<?php
declare(strict_types = 1);

namespace app\Core\Application\Command;

interface CommandBusInterface
{
    /**
     * @param $command
     */
    public function execute($command);
}